import { Component } from '@angular/core';
import { UserResponse } from '../../core/models/user-response.model';
import { AuthService } from '../../core/services/auth.service';
import { VerifyLoginRequest } from '../../core/models/verify-login-request.model';
import { TokenResponse } from '../../core/models/token-response.model';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-login-verification-code',
  templateUrl: './login-verification-code.component.html',
  styleUrls: ['./login-verification-code.component.css'],
})
export class LoginVerificationCodeComponent {
  constructor(private authService: AuthService, private router: Router) {}

  sendRecoveryCode($event: string) {
    let code: string = $event;
    let user: string | null = localStorage.getItem('userResponse');

    if (user === null) {
      alert("Error, you don't have permissions to login");
      return;
    }

    let userResponse: UserResponse = JSON.parse(user);
    let verificationLoginRequest: VerifyLoginRequest = {
      email: userResponse.email,
      code: code,
    };
    this.authService.verifyLogin(verificationLoginRequest).subscribe({
      next: (response: TokenResponse) => {
        localStorage.setItem('jwt', response.token);
        this.router.navigate(['certificate-view']);
      },
      error: (error) => {
        if (error instanceof HttpErrorResponse) {
          alert(JSON.stringify(error.error.message));
        }
      },
    });
  }
}
