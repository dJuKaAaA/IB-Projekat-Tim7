import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { ValidateCertificateDialogComponent } from '../validate-certificate-dialog/validate-certificate-dialog.component';
import { ValidateFromUploadComponent } from '../validate-from-upload/validate-from-upload.component';
import { DownloadCertificateComponent } from '../download-certificate/download-certificate.component';
import { AuthService } from 'src/app/core/services/auth.service';
import { PullCertificateDialogComponent } from '../pull-certificate-dialog/pull-certificate-dialog.component';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {

  constructor(
    private router: Router,
    private matDialog: MatDialog,
    public authService: AuthService
  ) {}

  public openValidationDialog() {
    this.matDialog.open(ValidateCertificateDialogComponent);
  }

  public openDownloadDialog(){
    this.matDialog.open(DownloadCertificateComponent);
  }

  public validateFromUpload() {
    this.matDialog.open(ValidateFromUploadComponent);
  }

  openPullDialog() {
    this.matDialog.open(PullCertificateDialogComponent);
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
    this.router.navigate(['demand-history'],{queryParams:{value:"history"}})
  }

  openEvaluateDemands(){
    this.router.navigate(['demand-history'], {queryParams:{value:"evaluation"}})
  }

  openAllDemands(){
    this.router.navigate(['certificate-demands'])
  }

  openResetPassword() {
    this.router.navigate(['reset-password'])
  }

}
