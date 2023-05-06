import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { ValidateCertificateDialogComponent } from '../validate-certificate-dialog/validate-certificate-dialog.component';
import { ValidateFromUploadComponent } from '../validate-from-upload/validate-from-upload.component';

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

  public validateFromUpload() {
    this.matDialog.open(ValidateFromUploadComponent);
  }

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

  openDemandHistory(){
    this.router.navigate(['demand-history'])
  }

}
