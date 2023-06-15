import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { LoginRequest } from '../models/login-request.model';
import { TokenResponse } from '../models/token-response.model';
import { UserRequest } from '../models/user-request.model';
import { UserResponse } from '../models/user-response.model';
import { JwtHelperService } from '@auth0/angular-jwt';
import { environment } from './../../../environment/environment';
import { VerifyVerificationCodeRequest } from '../models/verify-verification-code-request.mode';
import { VerificationTarget } from '../models/verification-target.model';
import { PasswordRecoveryRequest } from '../models/password-recovery-request.model';
import { PasswordResetRequest } from '../models/password-reset-request.model';
import { VerifyLoginRequest } from '../models/verify-login-request.model';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(private httpClient: HttpClient) { }

  public loginWithGithub() {
    return this.httpClient.get(`${environment.baseUrl}/auth/github/oauth`);
  }

  public sendVerificationCode(verificationTarget: VerificationTarget) {
    return this.httpClient.post(
      `${environment.baseUrl}/auth/sendVerificationCode`,
      verificationTarget
    );
  }

  public verifyVerificationCode(
    codeVerificationRequest: VerifyVerificationCodeRequest
  ) {
    return this.httpClient.post(
      `${environment.baseUrl}/auth/verifyVerificationCode`,
      codeVerificationRequest
    );
  }

  public login(
    loginRequest: LoginRequest,
    recaptchaResponse: string,
    verificationType: string
  ): Observable<UserResponse> {
    return this.httpClient.post<UserResponse>(
      `${environment.baseUrl}/auth/login/${verificationType}/?g-recaptcha-response=${recaptchaResponse}`,
      loginRequest
    );
  }

  public verifyLogin(
    verificationLoginRequest: VerifyLoginRequest
  ): Observable<TokenResponse> {
    return this.httpClient.post<TokenResponse>(
      `${environment.baseUrl}/auth/verifyLogin`,
      verificationLoginRequest
    );
  }

  public createAccount(
    userRequest: UserRequest,
    verificationType: string,
    recaptchaResponse: string
  ): Observable<UserResponse> {
    return this.httpClient.post<UserResponse>(
      `${environment.baseUrl}/auth/create-account/${verificationType}?g-recaptcha-response=${recaptchaResponse}`,
      userRequest
    );
  }

  public verifyRegistration(
    registrationVerificationRequest: VerifyVerificationCodeRequest
  ) {
    return this.httpClient.post(
      `${environment.baseUrl}/auth/verifyRegistration`,
      registrationVerificationRequest
    );
  }

  public recoverPassword(passwordRecoveryRequest: PasswordRecoveryRequest) {
    {
      return this.httpClient.post(
        `${environment.baseUrl}/auth/recoverPassword`,
        passwordRecoveryRequest
      );
    }
  }

  public resetPassword(passwordResetRequest: PasswordResetRequest) {
    {
      return this.httpClient.post(
        `${environment.baseUrl}/auth/resetPassword`,
        passwordResetRequest
      );
    }
  }

  public getRole(): string {
    if (this.isLoggedIn()) {
      const accessToken: any = localStorage.getItem('jwt');
      const helper = new JwtHelperService();
      const role = helper.decodeToken(accessToken).roles[0];
      return role;
    }
    return '';
  }

  public getId(): number {
    if (this.isLoggedIn()) {
      const accessToken: any = localStorage.getItem('jwt');
      const helper = new JwtHelperService();
      const id = helper.decodeToken(accessToken).id;
      return id;
    }
    return NaN;
  }

  public getEmail(): string {
    if (this.isLoggedIn()) {
      const accessToken: any = localStorage.getItem('jwtj');
      const helper = new JwtHelperService();
      const email = helper.decodeToken(accessToken).sub;
      return email;
    }
    return '';
  }

  private getToken(): string {
    if (this.isLoggedIn()) {
      const accessToken: any = localStorage.getItem('jwt');
      const decodedItem = JSON.parse(accessToken);
      return decodedItem.accessToken;
    }
    return '';
  }

  public isLoggedIn(): boolean {
    return localStorage.getItem('jwt') != null;
  }

  public loginWithGoogle(): Observable<TokenResponse> {
    return this.httpClient.post<TokenResponse>(`${environment.baseUrl}/auth/google/login`, {});
  }
}
