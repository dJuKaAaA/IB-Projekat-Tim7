import { HttpErrorResponse } from '@angular/common/http';
import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { CertificateDemandRequest } from 'src/app/core/models/certificate-demand-request.model';
import { CertificateDemandResponse } from 'src/app/core/models/certificate-demand-response.model';
import { AuthService } from 'src/app/core/services/auth.service';
import { CertificateDemandService } from 'src/app/core/services/certificate-demand.service';

@Component({
  selector: 'app-demand-creation-dialog',
  templateUrl: './demand-creation-dialog.component.html',
  styleUrls: ['./demand-creation-dialog.component.css']
})
export class DemandCreationDialogComponent {

  constructor(
    private dialogRef: MatDialogRef<DemandCreationDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private certificateDemandService: CertificateDemandService,
    private authService: AuthService,
  ) {}

  reason: string = "";
  certificateType: string = "";

  onSaveClick(): void {
    const certificateDemandRequest: CertificateDemandRequest = {
      type: this.certificateType,
      requestedSigningCertificateId: this.data.certificate.id,
      requesterId: this.authService.getId(),
      reason: this.reason
    }

    this.certificateDemandService.create(certificateDemandRequest).subscribe({
      next: (response: CertificateDemandResponse) => {
        alert("Demand created successfully!")
        this.dialogRef.close();
      }, error: (error) => {
        if (error instanceof HttpErrorResponse) {
          alert(error.error.message);
        }
      }
    })

  }

  onCancelClick(): void {
    
    this.dialogRef.close();
  }

  onCertificateTypeSelected(certificateType: string) {
    this.certificateType = certificateType;

  }

}
