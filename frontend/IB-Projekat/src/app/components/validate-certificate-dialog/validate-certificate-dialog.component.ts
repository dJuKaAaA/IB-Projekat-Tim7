import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/core/services/auth.service';
import { CertificateService } from 'src/app/core/services/certificate.service';

@Component({
  selector: 'app-validate-certificate-dialog',
  templateUrl: './validate-certificate-dialog.component.html',
  styleUrls: ['./validate-certificate-dialog.component.css']
})
export class ValidateCertificateDialogComponent {

  constructor(
    private dialogRef: MatDialogRef<ValidateCertificateDialogComponent>,
    private certificateService: CertificateService,
    private router: Router
  ) {}

  serialNumber: string = "";

  onCancelClick(): void {
    this.dialogRef.close();
  }

  validate() {
    this.certificateService.validate(this.serialNumber).subscribe({
      next: () => {
        alert("This certificate is valid!");
      }, error: (error) => {
        if (error instanceof HttpErrorResponse) {
          alert(error.error.message);
        }
      }
    })
  }

}
