import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { ValidateCertificateDialogComponent } from '../validate-certificate-dialog/validate-certificate-dialog.component';
import { CertificateService } from 'src/app/core/services/certificate.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-download-certificate',
  templateUrl: './download-certificate.component.html',
  styleUrls: ['./download-certificate.component.css']
})
export class DownloadCertificateComponent {
  constructor(
    private dialogRef: MatDialogRef<ValidateCertificateDialogComponent>,
    private certificateService: CertificateService
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

  download(){
    if(this.serialNumber == "")
      return;
    this.certificateService.download(this.serialNumber).subscribe(
      data => {
        const blob = new Blob([data], { type: 'application/x-x509-ca-cert' }); // change the MIME type to match your file type
        const downloadUrl = URL.createObjectURL(blob);

        const link = document.createElement('a');
        link.href = downloadUrl;
        link.download = this.serialNumber + '.crt';
        link.click();

        URL.revokeObjectURL(downloadUrl);
      }
    )
  }
}
