/*  app.js ‑ Slot‑Machine front‑end
 *  Works with Spring‑Boot REST backend (no security).
 *  Endpoints used:
 *    POST /api/create/{username}
 *    GET  /api/wallet/{username}
 *    POST /api/spin/{username}?stake=n
 *
 *  Stores the last username in localStorage so the player
 *  doesn’t have to enter it again next visit.
 */
const API = '/api';

// -------- DOM helpers -------------------------------------------------------
const $ = id => document.getElementById(id);
function showError(msg) { if ($('error')) $('error').textContent = msg; }
function clearError()   { showError(''); }

// -------- State -------------------------------------------------------------
let username =
  new URLSearchParams(location.search).get('user') ||
  localStorage.getItem('slotUser');

// -------- User creation / loading ------------------------------------------
function create() {
  const u = $('user').value.trim();
  if (!u) return showError('Enter a username');
  fetch('/api/create/' + u, { method: 'POST' })
    .then(r => r.ok ? r.text() : Promise.reject('User creation failed'))
    .then(() => {
      localStorage.setItem('slotUser', u);
      window.location.href = 'game.html?user=' + encodeURIComponent(u);
    })
    .catch(e => showError(e));
}


// -------- Wallet helpers ----------------------------------------------------
function loadBalance() {
  return fetch(`${API}/wallet/${encodeURIComponent(username)}`)
    .then(r => {
      if (!r.ok) throw new Error('Cannot fetch balance');
      return r.json();
    });
}

function updateBalance() {
  loadBalance()
    .then(bal => {
      if ($('balance')) $('balance').textContent = bal.balance;
    })
    .catch(err => showError(err.message));
}


// -------- Game logic --------------------------------------------------------
//function showGame() {
//  if (!$('game')) return;   // not on index page
//   if ($('user')) {                 // ⬅️ add this guard
//        $('user').value = username;
//   }// fill input for convenience
//  $('game').style.display = 'block';
//  updateBalance();
//}

function showGame() {
  /* remove this line:
  if (!$('game')) return;
  */
  updateBalance();   // call first so balance shows instantly

  // only fill user box if it exists (game page doesn’t have it)
  if ($('user')) $('user').value = username;
}

function spin() {
  clearError();
  const stake = parseInt($('stake').value, 10) || 0;
  if (stake <= 0) return showError('Stake must be greater than 0');
  if (!username || username === 'null') return showError('No user logged in!');
  fetch(`${API}/spin/${encodeURIComponent(username)}?stake=${stake}`, { method: 'POST' })
    .then(r => {
      if (!r.ok) return r.text().then(t => { throw new Error(t || 'Spin failed'); });
      return r.json();
    })
    // ...rest unchanged
    .then(data => {
          $('reels').textContent = data.reels.join(' | ');
          $('result').textContent = data.win
            ? `🎉 You won ₹${data.payout}`
            : '❌ You lost';
          $('result').style.color = data.win ? 'green' : 'red';
          $('stake').value = 1;        // reset stake box
          updateBalance();
        })
    .catch(err => showError(err.message));
}

// -------- Navigation --------------------------------------------------------
function gotoWallet() {
  window.location.href = `wallet.html?user=${encodeURIComponent(username)}`;
}

// -------- Wallet page init --------------------------------------------------
// Function to initialize the wallet page
function walletPageInit() {
    // Get the wallet username: first from URL query, else from localStorage
    let params = new URLSearchParams(window.location.search);
    let username = params.get('user') || localStorage.getItem('slotUser');

    // Fallback if username not found
    if (!username) {
        username = 'Unknown';
    }

    // Set username in the wallet page UI
    document.getElementById('user').textContent = username;

    // Call your function to update balance (presumed existing)
    updateBalance();
}


// -------- Auto‑initialise on load ------------------------------------------
document.addEventListener('DOMContentLoaded', () => {
  username =
    new URLSearchParams(location.search).get('user') ||
    localStorage.getItem('slotUser');

  if (!username) return;                       // stay on login page

  if (location.pathname.endsWith('game.html')) {
    showGame();                                // initialise game screen
  } else if (location.pathname.endsWith('wallet.html')) {
    walletPageInit();                          // wallet screen
  }
});