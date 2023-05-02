import { Component } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from './../../../core/services/auth.service';
import { VerificationTarget } from '../../../core/models/verification-target.model';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-password-recovery-step1',
  templateUrl: './password-recovery-step1.component.html',
  styleUrls: ['./password-recovery-step1.component.css'],
})
export class PasswordRecoveryStep1Component {
  formGroup: FormGroup = new FormGroup({
    verificationCodeType: new FormControl(''),
    codeTarget: new FormControl(''),
  });

  constructor(private authService: AuthService, private router: Router) {}

  sendRecoveryCode(): void {
    let verificationCodeType: string =
      this.formGroup.value.verificationCodeType.toLowerCase();
    let codeTarget: string = this.formGroup.value.codeTarget;

    let verificationTarget: VerificationTarget = {} as VerificationTarget;

    if (verificationCodeType === 'email') {
      verificationTarget.email = codeTarget;
      verificationTarget.phoneNumber = '';
    } else if (verificationCodeType === 'phone') {
      verificationTarget.email = '';
      verificationTarget.phoneNumber = codeTarget;
    }
    this.authService
      .sendVerificationCode(verificationTarget)

      .subscribe({
        next: (response) => {
          alert('We have send you recovery code!');
          localStorage.setItem('verificationCodeType', verificationCodeType);
          localStorage.setItem('codeTarget', codeTarget);
          this.navigateToPasswordRecoveryStep2();
        },
        error: (error) => {
          if (error instanceof HttpErrorResponse) {
            alert(error.error.message);
          }
        },
      });
  }

  navigateToPasswordRecoveryStep2() {
    this.router.navigate(['password-recovery-step2']);
  }
}
