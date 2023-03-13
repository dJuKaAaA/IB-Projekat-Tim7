import { Component } from '@angular/core';
import { LoginRequest } from '../../../model/login-request.model';
import { FormGroup, FormControl } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/service/auth.service';
import { TokenResponse } from 'src/app/model/token-response.model';
import { HttpErrorResponse } from '@angular/common/http';

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
          alert("Successful login!")
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
