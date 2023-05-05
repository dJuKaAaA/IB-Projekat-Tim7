export interface PasswordResetRequest {
  email: string;
  oldPassword: string;
  newPassword: string;
}
