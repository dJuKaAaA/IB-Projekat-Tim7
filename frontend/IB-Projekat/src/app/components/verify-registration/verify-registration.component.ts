import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { RegistrationVerificationRequest } from '../../core/models/regisration-verification-request.mode';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-verify-registration',
  templateUrl: './verify-registration.component.html',
  styleUrls: ['./verify-registration.component.css'],
})
export class VerifyRegistrationComponent {
  formGroup: FormGroup = new FormGroup({
    code: new FormControl(''),
  });

  constructor(private authService: AuthService, private router: Router) {}

  public verifyAccount(): void {
    try {
      if (localStorage.getItem('email') == null) {
        throw new Error('You can not send verification code!');
      }
    } catch (error: any) {
      alert(error);
      return;
    }
    let registrationVerificationRequest: RegistrationVerificationRequest = {
      code: this.formGroup.value.code,
      email: localStorage.getItem('email')!,
    };

    this.authService.verifyAccount(registrationVerificationRequest).subscribe({
      next: () => {
        alert('You have successfully verified account!');
        this.navigateToLogin();
      },
      error: (error) => {
        alert(error.error.message);
      },
    });
  }

  navigateToLogin() {
    this.router.navigate(['login']);
  }
}
