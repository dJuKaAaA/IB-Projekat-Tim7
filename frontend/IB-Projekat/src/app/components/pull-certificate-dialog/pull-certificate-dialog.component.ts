import { Component } from '@angular/core';
import { ValidateCertificateDialogComponent } from '../validate-certificate-dialog/validate-certificate-dialog.component';
import { MatDialogRef } from '@angular/material/dialog';
import { CertificateService } from 'src/app/core/services/certificate.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-pull-certificate-dialog',
  templateUrl: './pull-certificate-dialog.component.html',
  styleUrls: ['./pull-certificate-dialog.component.css']
})
export class PullCertificateDialogComponent {

  constructor(
    private dialogRef: MatDialogRef<ValidateCertificateDialogComponent>,
    private certificateService: CertificateService
  ) {}

  serialNumber: string = "";

  onCancelClick(): void {
    this.dialogRef.close();
  }

  pullCertificate(){
    if (confirm(`Are you sure you want to pull the certificate with serial number: "${this.serialNumber}"?`)) {
      this.certificateService.pull(this.serialNumber).subscribe({
        next: () => {
          alert("Certificate, and the ones in it's downward chain, have been pulled successfully!")
          this.dialogRef.close();
        }, error: (error) => {
          if (error instanceof HttpErrorResponse) {
            alert(error.error.message);
          }
        }
      })
    }
  }
}
