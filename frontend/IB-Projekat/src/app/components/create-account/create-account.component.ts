import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import {
  FormGroup,
  FormControl,
  Validators,
  FormBuilder,
} from '@angular/forms';
import { Router } from '@angular/router';
import { UserRequest } from 'src/app/core/models/user-request.model';
import { UserResponse } from 'src/app/core/models/user-response.model';
import { AuthService } from 'src/app/core/services/auth.service';
import { VerifyVerificationCodeRequest } from '../../core/models/verify-verification-code-request.mode';
import { ToastrService } from 'ngx-toastr';
import { MatSnackBar } from '@angular/material/snack-bar';
import { environment } from '../../../environment/environment';

@Component({
  selector: 'app-create-account',
  templateUrl: './create-account.component.html',
  styleUrls: ['./create-account.component.css'],
})
export class CreateAccountComponent {
  public siteKey: string = environment.recaptchaSiteKey;
  private formBuilder: FormBuilder = new FormBuilder();
  isRecaptchaVerified: boolean = false;

  public formGroup: FormGroup = this.formBuilder.group({
    name: ['', Validators.required],
    surname: ['', Validators.required],
    email: ['', Validators.required],
    password: ['', Validators.required],
    confirmPassword: ['', Validators.required],
    verificationCodeType: ['', Validators.required],
    phoneNumber: ['', Validators.required],
    recaptcha: ['', Validators.required],
  });

  constructor(private authService: AuthService, private router: Router) {}

  createAccount() {
    if (this.formGroup.value.password != this.formGroup.value.confirmPassword) {
      alert('Passwords not matching!');
      return;
    }

    if (this.formGroup.valid || this.isRecaptchaVerified) {
      const userRequest: UserRequest = {
        name: this.formGroup.value.name,
        surname: this.formGroup.value.surname,
        email: this.formGroup.value.email,
        password: this.formGroup.value.password,
        phoneNumber: this.formGroup.value.phoneNumber,
      };

      const recaptchaResponse: string = this.formGroup.value.recaptcha;

      if (!this.isRecaptchaVerified) {
        this.setRecaptchaVerified();
        this.authService
          .createAccount(
            userRequest,
            this.formGroup.value.verificationCodeType,
            recaptchaResponse
          )
          .subscribe({
            next: (response: UserResponse) => {
              alert('Successfully created account! Just one more step!');
              localStorage.setItem('email', this.formGroup.value.email);
              this.navigateToVerifyRegistrationPage();
            },
            error: (error) => {
              if (error instanceof HttpErrorResponse) {
                alert(error.error.message);
              }
            },
          });
      } else {
        this.authService
          .createAccount(
            userRequest,
            this.formGroup.value.verificationCodeType,
            ''
          )
          .subscribe({
            next: (response: UserResponse) => {
              alert('Successfully created account! Just one more step!');
              localStorage.setItem('email', this.formGroup.value.email);
              this.navigateToVerifyRegistrationPage();
            },
            error: (error) => {
              if (error instanceof HttpErrorResponse) {
                alert(error.error.message);
              }
            },
          });
      }
    }
  }

  navigateBack() {
    this.router.navigate(['login']);
  }
  navigateToVerifyRegistrationPage() {
    this.router.navigate(['verify-registration']);
  }
  setRecaptchaVerified() {
    this.isRecaptchaVerified = true;
  }
}
