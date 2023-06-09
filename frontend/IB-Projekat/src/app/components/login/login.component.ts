import { Component } from '@angular/core';
import {
  FormGroup,
  FormControl,
  Validators,
  FormBuilder,
} from '@angular/forms';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { LoginRequest } from 'src/app/core/models/login-request.model';
import { AuthService } from 'src/app/core/services/auth.service';
import { TokenResponse } from 'src/app/core/models/token-response.model';
import { environment } from '../../../environment/environment';
import { UserResponse } from '../../core/models/user-response.model';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent {
  public siteKey: string = environment.recaptchaSiteKey;
  private formBuilder: FormBuilder = new FormBuilder();
  isRecaptchaVerified: boolean = false;
  loginButtonDisabled: boolean = false;

  public formGroup: FormGroup = this.formBuilder.group({
    email: ['', Validators.required],
    password: ['', Validators.required],
    recaptcha: ['', Validators.required],
    verificationCodeType: ['', Validators.required],
  });

  constructor(private router: Router, private authService: AuthService) {}

  login() {
    this.loginButtonDisabled = true;

    if (this.formGroup.value.recaptcha == '') {
      alert("Prove you are not a robot!");
      this.loginButtonDisabled = false;
      return;
    }
    
    if (this.formGroup.valid) {
      const loginRequest: LoginRequest = {
        email: this.formGroup.value.email,
        password: this.formGroup.value.password,
      };

      const recaptchaResponse: string = this.formGroup.value.recaptcha;

      if (!this.isRecaptchaVerified) {
        this.setRecaptchaVerified();
        this.loginWithRecaptcha(loginRequest, recaptchaResponse);
      } else {
        this.loginWithRecaptcha(loginRequest, '');
      }
    } else {
      this.loginButtonDisabled = false;
    }
  }

  loginWithRecaptcha(loginRequest: LoginRequest, recaptchaResponse: string) {
    let verificationCodeType: string =
      this.formGroup.value.verificationCodeType;
    this.authService
      .login(loginRequest, recaptchaResponse, verificationCodeType)
      .subscribe({
        next: (response: UserResponse) => {
          console.log(response);
          localStorage.setItem('userResponse', JSON.stringify(response));
          this.router.navigate(['verify-login']);
        },
        error: (error) => {
          if (error instanceof HttpErrorResponse) {
            alert(JSON.stringify(error.error.message));
            this.loginButtonDisabled = false;

            if (error.error.message === 'The password is outdated') {
              localStorage.setItem('email', this.formGroup.value.email);
              this.goToResetPasswordPage();
            }
          }
        },
      });
  }

  loginWithGitHub(): void {
    window.location.href = `${environment.baseUrl}/auth/github/oauth`;
  }

  goToResetPasswordPage() {
    this.router.navigate(['reset-password']);
  }

  goToCreateAccount() {
    this.router.navigate(['create-account']);
  }

  goToResetPassword() {
    this.router.navigate(['password-recovery-step1']);
  }

  setRecaptchaVerified() {
    this.isRecaptchaVerified = true;
  }
}
