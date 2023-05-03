import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { UploadedCertificateRequest } from 'src/app/core/models/uploaded-certificate-request.model';
import { CertificateService } from 'src/app/core/services/certificate.service';

@Component({
  selector: 'app-validate-from-upload',
  templateUrl: './validate-from-upload.component.html',
  styleUrls: ['./validate-from-upload.component.css']
})
export class ValidateFromUploadComponent {

  constructor(
    private dialogRef: MatDialogRef<ValidateFromUploadComponent>,
    private certificateService: CertificateService
  ) {}

  certPath: string = "";
  base64Cert: string = "";

  onCancelClick(): void {
    this.dialogRef.close();
  }

  onFileSelected(event: any) {
    const selectedFile = event.target.files[0];
    const reader = new FileReader();
    reader.onload = (e: any) => {
      const base64String = btoa(e.target.result);
      this.base64Cert = base64String;
    };
    reader.readAsBinaryString(selectedFile);
  }

  validate() {
    const uploadedCertificateRequest: UploadedCertificateRequest = {
      base64Certificate: this.base64Cert
    }
    this.certificateService.validateFromUpload(uploadedCertificateRequest).subscribe({
      next: () => {
        alert("The certificate is valid!")
      }, error: (error) => {
        if (error instanceof HttpErrorResponse) {
          alert(error.error.message);
        }
      }
    });

  }
  

}
