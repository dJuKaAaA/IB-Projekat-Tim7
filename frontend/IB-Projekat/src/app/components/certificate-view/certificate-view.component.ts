import { Component, EventEmitter, OnInit } from '@angular/core';
import { MaxLengthValidator } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { CertificateResponse } from 'src/app/core/models/certificate-response.model';
import { PaginatedResponse } from 'src/app/core/models/paginated-response.model';
import { CertificateService } from 'src/app/core/services/certificate.service';
import { DemandCreationDialogComponent } from '../demand-creation-dialog/demand-creation-dialog.component';

@Component({
  selector: 'app-certificate-view',
  templateUrl: './certificate-view.component.html',
  styleUrls: ['./certificate-view.component.css']
})
export class CertificateViewComponent implements OnInit {

  allCertificates: Array<CertificateResponse> = [];

  constructor(
    private certificateService: CertificateService,
    private matDialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.certificateService.getAll(0, 10).subscribe((response: PaginatedResponse<CertificateResponse>) => {
      this.allCertificates = response.content;
    })
  }

  createDemand(certificate: CertificateResponse) {
    this.matDialog.open(DemandCreationDialogComponent, {
      data: {
        certificate: certificate
      },
    });
  }



}
