import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'environment/environment';
import { Observable } from 'rxjs';
import { LoginRequest } from '../model/login-request.model';
import { TokenResponse } from '../model/token-response.model';
import { UserRequest } from '../model/user-request.model';
import { UserResponse } from '../model/user-response.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(
    private httpClient: HttpClient
  ) { }

  public login(loginRequest: LoginRequest): Observable<TokenResponse> {
    return this.httpClient.post<TokenResponse>(`${environment.baseUrl}/auth/login`, loginRequest);
  }

  public createAccount(userRequest: UserRequest): Observable<UserResponse> {
    return this.httpClient.post<UserResponse>(`${environment.baseUrl}/auth/create-account`, userRequest);
  }

}
