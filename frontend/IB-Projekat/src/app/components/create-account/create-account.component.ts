import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { Router } from '@angular/router';
import { UserRequest } from 'src/app/core/models/user-request.model';
import { UserResponse } from 'src/app/core/models/user-response.model';
import { AuthService } from 'src/app/core/services/auth.service';

@Component({
  selector: 'app-create-account',
  templateUrl: './create-account.component.html',
  styleUrls: ['./create-account.component.css']
})
export class CreateAccountComponent {

  formGroup: FormGroup = new FormGroup({
    name: new FormControl(""),
    surname: new FormControl(""),
    email: new FormControl(""),
    password: new FormControl(""),
    confirmPassword: new FormControl(""),
  });

  constructor(
    private authService: AuthService,
    private router: Router
  ) { }

  createAccount() {
    if (this.formGroup.value.password != this.formGroup.value.confirmPassword) {
      alert("Passwords not matching!")
      return;
    }

    if (this.formGroup.valid) {
      const userRequest: UserRequest = {
        name: this.formGroup.value.name,
        surname: this.formGroup.value.surname,
        email: this.formGroup.value.email,
        password: this.formGroup.value.password,
      }
      this.authService.createAccount(userRequest).subscribe({
        next: (response: UserResponse) => {
          alert("Successfully created account!")
          this.navigateBack();
        },
        error: (error) => {
          if (error instanceof HttpErrorResponse) {
            alert(error.error.message);
          }
        }
      })
    }

  }

  navigateBack() {
    this.router.navigate(['login']);
  }

}
