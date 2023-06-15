export interface PasswordRecoveryRequest {
  userEmail: string;
  userPhoneNumber: string;
  newPassword: string;
}
