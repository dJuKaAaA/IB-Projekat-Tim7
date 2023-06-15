import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';
import { TokenResponse } from 'src/app/core/models/token-response.model';
import { AuthService } from 'src/app/core/services/auth.service';

@Component({
  selector: 'app-oauth-login',
  templateUrl: './oauth-login.component.html',
  styleUrls: ['./oauth-login.component.css']
})
export class OAuthLoginComponent implements OnInit {

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}
  
  ngOnInit(): void {
    this.authService.loginWithGoogle().subscribe({
      next: (response: TokenResponse) => {
        if (response.token == undefined) {
          this.router.navigate(["login"]);
          return;
        }

        localStorage.setItem('jwt', response.token);
        this.router.navigate(['certificate-view']);
      }, error: error => {
        if (error instanceof HttpErrorResponse) {
          alert("Error has occured");
        }
      }
    });
  }


  
}
