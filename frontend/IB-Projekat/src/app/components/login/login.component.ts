import { Component } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { LoginRequest } from 'src/app/core/models/login-request.model';
import { AuthService } from 'src/app/core/services/auth.service';
import { TokenResponse } from 'src/app/core/models/token-response.model';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  formGroup: FormGroup = new FormGroup({
    email: new FormControl(""),
    password: new FormControl(""),
  });

  constructor(
    private router: Router,
    private authService: AuthService
  ) { }

  login() {
    if (this.formGroup.valid) {
      const loginRequest: LoginRequest = {
        email: this.formGroup.value.email,
        password: this.formGroup.value.password
      }
      this.authService.login(loginRequest).subscribe({
        next: (response: TokenResponse) => {
          localStorage.setItem('jwt', response.token);
          this.router.navigate(['certificate-view']);
        },
        error: (error) => {
          if (error instanceof HttpErrorResponse) {
            alert(JSON.stringify(error.error.message));
          }
        }
      })
    }
  }

  goToCreateAccount() {
    this.router.navigate(['create-account'])
  }

}
