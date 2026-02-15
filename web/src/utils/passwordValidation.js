/**
 * Password validation rules - at least 8 chars, uppercase, lowercase, numbers, special chars
 */
export const PASSWORD_RULES = [
  { id: 'length', label: 'At least 8 characters long', test: (p) => p.length >= 8 },
  { id: 'lowercase', label: 'Mix of uppercase and lowercase characters', test: (p) => /[a-z]/.test(p) && /[A-Z]/.test(p) },
  { id: 'number', label: 'Contains numbers', test: (p) => /\d/.test(p) },
  { id: 'special', label: 'Contains special characters (e.g. !@#$%)', test: (p) => /[!@#$%^&*()_+\-=[\]{};':"\\|,.<>/?]/.test(p) },
];

export function validatePassword(password) {
  return PASSWORD_RULES.every((rule) => rule.test(password));
}

export function getPasswordRuleResults(password) {
  return PASSWORD_RULES.map((rule) => ({
    ...rule,
    passed: rule.test(password),
  }));
}
