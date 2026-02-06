import './LogoutModal.css';

export default function LogoutModal({ user, onKeepSignedIn, onConfirmSignOut }) {
  return (
    <div className="logout-modal-overlay" onClick={onKeepSignedIn}>
      <div className="logout-modal" onClick={(e) => e.stopPropagation()}>
        <h3>Sign out?</h3>
        <p className="logout-modal-user">
          Currently signed in as <strong>{user?.username}</strong>
        </p>
        {user?.email && (
          <p className="logout-modal-email">{user.email}</p>
        )}
        <div className="logout-modal-actions">
          <button type="button" className="logout-btn-keep" onClick={onKeepSignedIn}>
            Keep signed-in
          </button>
          <button type="button" className="logout-btn-confirm" onClick={onConfirmSignOut}>
            Confirm sign-out
          </button>
        </div>
      </div>
    </div>
  );
}
