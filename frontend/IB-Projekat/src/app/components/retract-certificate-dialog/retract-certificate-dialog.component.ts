import { Component } from '@angular/core';
import { ValidateCertificateDialogComponent } from '../validate-certificate-dialog/validate-certificate-dialog.component';
import { MatDialogRef } from '@angular/material/dialog';
import { CertificateService } from 'src/app/core/services/certificate.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-retract-certificate-dialog',
  templateUrl: './retract-certificate-dialog.component.html',
  styleUrls: ['./retract-certificate-dialog.component.css']
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

  retractCertificate(){
    if (confirm(`Are you sure you want to pull the certificate with serial number: "${this.serialNumber}"?`)) {
      this.certificateService.retract(this.serialNumber).subscribe({
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
