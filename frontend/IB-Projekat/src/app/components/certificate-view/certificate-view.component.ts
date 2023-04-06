import { Component, OnInit } from '@angular/core';
import { CertificateResponse } from 'src/app/core/models/certificate-response.model';
import { PaginatedResponse } from 'src/app/core/models/paginated-response.model';
import { CertificateService } from 'src/app/core/services/certificate.service';

@Component({
  selector: 'app-certificate-view',
  templateUrl: './certificate-view.component.html',
  styleUrls: ['./certificate-view.component.css']
})
export class CertificateViewComponent implements OnInit {

  allCertificates: Array<CertificateResponse> = [];

  constructor(
    private certificateService: CertificateService
  ) {}

  ngOnInit(): void {
    this.certificateService.getAll(0, 10).subscribe((response: PaginatedResponse<CertificateResponse>) => {
      this.allCertificates = response.content;
    })
  }

}
