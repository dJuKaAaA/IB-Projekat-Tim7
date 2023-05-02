import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {

  constructor(
    private router: Router
  ) {}

  public logout() {
    if (confirm("Log out?")) {
      localStorage.removeItem("jwt");
      this.router.navigate(['login'])
    }
  }

  openAllCertificates() {
    this.router.navigate(['certificate-view'])
  }

  openMyCertificates() {
    this.router.navigate(['my-certificates'])
  }

}
