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

  download(){
    if(this.serialNumber == "")
      return;
    this.certificateService.download(this.serialNumber).subscribe({
      next: response => {
        const certificateBlob = new Blob([response.certificateBytes], { type: 'application/x-x509-ca-cert' }); 
        const downloadUrl = URL.createObjectURL(certificateBlob);

        const link = document.createElement('a');
        link.href = downloadUrl;
        link.download = this.serialNumber + '.crt';
        link.click();

        URL.revokeObjectURL(downloadUrl);

        if (response.certificatePrivateKeyBytes != undefined) {
          const privateKeyBlob = new Blob([response.certificatePrivateKeyBytes], { type: 'application/octet-stream' });
          const privateKeyDownloadUrl = URL.createObjectURL(privateKeyBlob);

          const privateKeyLink = document.createElement('a');
          privateKeyLink.href = privateKeyDownloadUrl;
          privateKeyLink.download = this.serialNumber + '.key';  // change to .pfx if necessary
          privateKeyLink.click();

          URL.revokeObjectURL(privateKeyDownloadUrl);
        }
      }, error: (error) => {
        if (error instanceof HttpErrorResponse) {
          // for some reason the front and back won't communicate and return the proper
          // error message for this method so I am forced to hard code it :(
          alert(error.error.message);
        }
      }
    })
  }
}
