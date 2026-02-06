import { useState } from 'react';
import { useAuth } from '../../context/AuthContext';
import { useNavigate, NavLink } from 'react-router-dom';
import LogoutModal from '../LogoutModal/LogoutModal';
import './Header.css';

export default function Header() {
  const { currentUser, logout } = useAuth();
  const navigate = useNavigate();
  const [showLogoutModal, setShowLogoutModal] = useState(false);

  const handleLogoutClick = () => {
    setShowLogoutModal(true);
  };

  const handleKeepSignedIn = () => {
    setShowLogoutModal(false);
  };

  const handleConfirmSignOut = async () => {
    await logout();
    setShowLogoutModal(false);
    navigate('/login');
  };

  return (
    <header className="app-header">
      <h1>⏳ Employee Timesheet Tracker</h1>
      <nav className="header-nav">
        {currentUser ? (
          <>
            <span className="header-user">Hello, {currentUser.username}</span>
            <button type="button" className="header-logout" onClick={handleLogoutClick}>
              Logout
            </button>
          </>
        ) : (
          <>
            <NavLink to="/login" className={({ isActive }) => (isActive ? 'header-link active' : 'header-link')}>
              Login
            </NavLink>
            <NavLink to="/register" className={({ isActive }) => (isActive ? 'header-link active' : 'header-link')}>
              Register
            </NavLink>
          </>
        )}
      </nav>
      {showLogoutModal && (
        <LogoutModal
          user={currentUser}
          onKeepSignedIn={handleKeepSignedIn}
          onConfirmSignOut={handleConfirmSignOut}
        />
      )}
    </header>
  );
}
