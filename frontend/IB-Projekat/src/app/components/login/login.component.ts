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

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent {
  public siteKey: string = environment.recaptchaSiteKey;
  private formBuilder: FormBuilder = new FormBuilder();
  isRecaptchaVerified: boolean = false;

  public formGroup: FormGroup = this.formBuilder.group({
    email: ['', Validators.required],
    password: ['', Validators.required],
    recaptcha: ['', Validators.required],
  });

  constructor(private router: Router, private authService: AuthService) {}

  login() {
    if (this.formGroup.valid || this.isRecaptchaVerified) {
      const loginRequest: LoginRequest = {
        email: this.formGroup.value.email,
        password: this.formGroup.value.password,
      };

      const recaptchaResponse: string = this.formGroup.value.recaptcha;

      if (!this.isRecaptchaVerified) {
        this.setRecaptchaVerified();

        this.authService.login(loginRequest, recaptchaResponse).subscribe({
          next: (response: TokenResponse) => {
            localStorage.setItem('jwt', response.token);
            this.router.navigate(['certificate-view']);
          },
          error: (error) => {
            if (error instanceof HttpErrorResponse) {
              alert(JSON.stringify(error.error.message));

              if (error.error.message === 'The password is outdated') {
                localStorage.setItem('email', this.formGroup.value.email);
                this.goToResetPasswordPage();
              }
            }
          },
        });
      } else {
        this.authService.login(loginRequest, '').subscribe({
          next: (response: TokenResponse) => {
            localStorage.setItem('jwt', response.token);
            this.router.navigate(['certificate-view']);
          },
          error: (error) => {
            if (error instanceof HttpErrorResponse) {
              alert(JSON.stringify(error.error.message));

              if (error.error.message === 'The password is outdated') {
                localStorage.setItem('email', this.formGroup.value.email);
                this.goToResetPasswordPage();
              }
            }
          },
        });
      }
    }
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
