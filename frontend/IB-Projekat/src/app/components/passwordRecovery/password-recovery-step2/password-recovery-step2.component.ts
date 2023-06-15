import { Component } from '@angular/core';
import { AuthService } from '../../../core/services/auth.service';
import { Router } from '@angular/router';
import { VerificationTarget } from '../../../core/models/verification-target.model';
import { VerifyVerificationCodeRequest } from '../../../core/models/verify-verification-code-request.mode';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-password-recovery-step2',
  templateUrl: './password-recovery-step2.component.html',
  styleUrls: ['./password-recovery-step2.component.css'],
})
export class PasswordRecoveryStep2Component {
  constructor(private authService: AuthService, private router: Router) {}

  sendRecoveryCode($event: string) {
    let verificationCodeType: string = localStorage
      .getItem('verificationCodeType')
      ?.toLowerCase()!;
    let codeTarget: string = localStorage.getItem('codeTarget')?.toLowerCase()!;

    let recoveryCode: string = $event;
    let verificationCodeRequest: VerifyVerificationCodeRequest =
      {} as VerifyVerificationCodeRequest;
    verificationCodeRequest.code = recoveryCode;

    if (verificationCodeType === 'email') {
      verificationCodeRequest.email = codeTarget;
    } else if (verificationCodeType === 'phone') {
      verificationCodeRequest.phoneNumber = codeTarget;
    }

    this.authService
      .verifyVerificationCode(verificationCodeRequest)

      .subscribe({
        next: (response) => {
          alert('We have successfully verified your code!');
          localStorage.setItem('verificationCodeType', verificationCodeType);
          localStorage.setItem('codeTarget', codeTarget);
          this.navigateToPasswordRecoveryStep3();
        },
        error: (error) => {
          if (error instanceof HttpErrorResponse) {
            alert(error.error.message);
          }
        },
      });
  }

  navigateToPasswordRecoveryStep3() {
    this.router.navigate(['password-recovery-step3']);
  }
}
