import { useState } from 'react';
import { useNavigate, NavLink, Navigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { getPasswordRuleResults } from '../../utils/passwordValidation';
import './RegisterPage.css';

export default function RegisterPage() {
  const { currentUser, register } = useAuth();
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const passwordRules = getPasswordRuleResults(password);
  const isPasswordValid = passwordRules.every((r) => r.passed);

  if (currentUser) {
    return <Navigate to="/" replace />;
  }

  const normalizeError = (msg) => {
    if (msg === 'User already exists' || msg === 'Email already exists') {
      return 'User already exists.';
    }
    return msg;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    if (password !== confirmPassword) {
      setError('Passwords do not match.');
      return;
    }

    if (!isPasswordValid) {
      setError('Password does not meet all requirements.');
      return;
    }

    const result = await register(name.trim(), email.trim(), password);
    if (result.success) {
      navigate('/');
    } else {
      setError(normalizeError(result.error));
    }
  };

  return (
    <div className="auth-page">
      <section className="auth-card">
        <h2>Register</h2>
        <form onSubmit={handleSubmit}>
          {error && <div className="auth-error">{error}</div>}
          <label htmlFor="register-name">Username</label>
          <input
            id="register-name"
            type="text"
            value={name}
            onChange={(e) => setName(e.target.value)}
            placeholder="Enter username"
            required
          />
          <label htmlFor="register-email">Email</label>
          <input
            id="register-email"
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            placeholder="Enter your email"
            required
          />
          <label htmlFor="register-password">Password</label>
          <input
            id="register-password"
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            placeholder="Enter your password"
            required
          />
          <ul className="password-warnings">
            {passwordRules.map((rule) => (
              <li key={rule.id} className={rule.passed ? 'passed' : ''}>
                {rule.passed ? '✓' : '○'} {rule.label}
              </li>
            ))}
          </ul>
          <label htmlFor="register-confirm">Confirm Password</label>
          <input
            id="register-confirm"
            type="password"
            value={confirmPassword}
            onChange={(e) => setConfirmPassword(e.target.value)}
            placeholder="Confirm your password"
            required
          />
          <button type="submit">Register</button>
        </form>
        <p className="auth-footer">
          Already have an account? <NavLink to="/login">Login</NavLink>
        </p>
      </section>
    </div>
  );
}
