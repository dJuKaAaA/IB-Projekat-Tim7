import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { ValidateCertificateDialogComponent } from '../validate-certificate-dialog/validate-certificate-dialog.component';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {

  constructor(
    private router: Router,
    private matDialog: MatDialog
  ) {}

  public openValidationDialog() {
    this.matDialog.open(ValidateCertificateDialogComponent);
  }

  public logout() {
    if (confirm("Log out?")) {
      localStorage.removeItem("jwt");
      this.router.navigate(['login'])
    }
  }

}
