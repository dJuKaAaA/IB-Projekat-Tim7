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

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(private httpClient: HttpClient) {}

  public sendVerificationCode(verificationTarget: VerificationTarget) {
    return this.httpClient.post<TokenResponse>(
      `${environment.baseUrl}/auth/sendVerificationCode`,
      verificationTarget
    );
  }

  public verifyVerificationCode(
    codeVerificationRequest: VerifyVerificationCodeRequest
  ) {
    return this.httpClient.post<TokenResponse>(
      `${environment.baseUrl}/auth/verifyVerificationCode`,
      codeVerificationRequest
    );
  }

  public login(loginRequest: LoginRequest): Observable<TokenResponse> {
    return this.httpClient.post<TokenResponse>(
      `${environment.baseUrl}/auth/login`,
      loginRequest
    );
  }

  public createAccount(
    userRequest: UserRequest,
    verificationType: string
  ): Observable<UserResponse> {
    return this.httpClient.post<UserResponse>(
      `${environment.baseUrl}/auth/create-account/${verificationType}`,
      userRequest
    );
  }

  public verifyRegistration(
    registrationVerificationRequest: VerifyVerificationCodeRequest
  ) {
    return this.httpClient.post<TokenResponse>(
      `${environment.baseUrl}/auth/verifyRegistration`,
      registrationVerificationRequest
    );
  }

  public recoverPassword(passwordRecoveryRequest: PasswordRecoveryRequest) {
    {
      return this.httpClient.post<TokenResponse>(
        `${environment.baseUrl}/auth/recoverPassword`,
        passwordRecoveryRequest
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
}
