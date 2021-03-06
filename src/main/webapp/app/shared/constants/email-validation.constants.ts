export const PATTERN_DOMAIN =
  '^(([a-zA-Z]{1})|([a-zA-Z]{1}[a-zA-Z]{1})|([a-zA-Z]{1}[0-9]{1})|([0-9]{1}[a-zA-Z]{1})|([a-zA-Z0-9][a-zA-Z0-9-_]{1,61}[a-zA-Z0-9])).([a-zA-Z]{2,6}|[a-zA-Z0-9-]{2,30}.[a-zA-Z]{2,3})$';
export const PATTERN_NAME = "^[-a-z0-9~!$%^&*_=+}{'?]+(.[-a-z0-9~!$%^&*_=+}{'?]+)*$";

export const KEYCODE_PROTECTED = [8, 9, 17, 18, 35, 36, 37, 38, 39, 40, 45];

// popular email domains for test
export const EMAIL_PROVIDERS = [
  'localhost',
  'aol.com',
  'gmail.com',
  'yahoo.com',
  'hotmail.com',
  'msn.com',
  'outlook.com',
  'live.com',
  'icloud.com',
  'sina.com'
];

// only sandbox test is available since my account blocked
export const EMAIL_VERIFY_API_KEY = 'test_2e4ced2da6d4522b6a2044bab5ab861c3baa80f3909df543e66da778aff9ed4f';
