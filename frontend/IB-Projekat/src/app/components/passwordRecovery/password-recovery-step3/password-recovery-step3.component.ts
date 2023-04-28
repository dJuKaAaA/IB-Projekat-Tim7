import { Component } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { PasswordRecoveryRequest } from './../../../core/models/password-recovery-request.model';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-password-recovery-step3',
  templateUrl: './password-recovery-step3.component.html',
  styleUrls: ['./password-recovery-step3.component.css'],
})
export class PasswordRecoveryStep3Component {
  formGroup: FormGroup = new FormGroup({
    password: new FormControl(''),
    confirmPassword: new FormControl(''),
  });

  constructor(private authService: AuthService, private router: Router) {}

  recoverPassword(): void {
    let password: string = this.formGroup.value.password;
    let confirmPassword: string = this.formGroup.value.confirmPassword;

    if (password != confirmPassword) {
      alert('Passwords not matching!');
      return;
    }

    let verificationCodeType: string = localStorage
      .getItem('verificationCodeType')
      ?.toLowerCase()!;
    let codeTarget: string = localStorage.getItem('codeTarget')?.toLowerCase()!;

    let passwordRecoveryRequest: PasswordRecoveryRequest =
      {} as PasswordRecoveryRequest;
    passwordRecoveryRequest.newPassword = this.formGroup.value.password;

    if (verificationCodeType === 'email') {
      passwordRecoveryRequest.userEmail = codeTarget;
    } else if (verificationCodeType === 'phone') {
      passwordRecoveryRequest.userPhoneNumber = codeTarget;
    }

    this.authService.recoverPassword(passwordRecoveryRequest).subscribe({
      next: (response) => {
        alert('You have successfully changed yor password');
        this.navigateToLogin();
      },
      error: (error) => {
        if (error instanceof HttpErrorResponse) {
          alert(error.error.message);
        }
      },
    });
  }

  navigateToLogin(): void {
    this.router.navigate(['login']);
  }
}
