import { Component } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from './../../core/services/auth.service';
import { PasswordResetRequest } from '../../core/models/password-reset-request.model';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css'],
})
export class ResetPasswordComponent {
  formGroup: FormGroup = new FormGroup({
    oldPassword: new FormControl(''),
    newPassword: new FormControl(''),
    confirmPassword: new FormControl(''),
  });

  constructor(private authService: AuthService, private router: Router) {}

  public resetPassword(): void {
    let email: string | null = localStorage.getItem('email');
    let oldPassword: string = this.formGroup.value.oldPassword;
    let newPassword: string = this.formGroup.value.newPassword;
    let confirmPassword: string = this.formGroup.value.confirmPassword;

    if (email == null) {
      alert('You can not change password!');
      return;
    }
    if (newPassword != confirmPassword) {
      alert('Passwords not matching!');
      return;
    }

    let passwordResetRequest: PasswordResetRequest = {
      email: email,
      oldPassword: oldPassword,
      newPassword: newPassword,
    };

    if (confirm("You will be logged out. Are you sure?")) {
      this.authService.resetPassword(passwordResetRequest).subscribe({
        next: (response) => {
          alert('You have successfully changed password!');
          localStorage.removeItem("jwt");
          this.router.navigate(['login'])
        },
        error: (error) => {
          if (error instanceof HttpErrorResponse) {
            alert(JSON.stringify(error.error.message));
          }
        },
      });
    }
  }
}
