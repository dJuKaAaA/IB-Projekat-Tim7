import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { PaginatedResponse } from '../models/paginated-response.model';
import { CertificateResponse } from '../models/certificate-response.model';
import { Observable } from 'rxjs';
import { environment } from 'src/environment/environment';
import { UploadedCertificateRequest } from '../models/uploaded-certificate-request.model';

@Injectable({
  providedIn: 'root'
})
export class CertificateService {

  constructor(
    private httpClient: HttpClient
  ) { }

  public getAll(page: number, size: number): Observable<PaginatedResponse<CertificateResponse>> {
    return this.httpClient.get<PaginatedResponse<CertificateResponse>>(`${environment.baseUrl}/certificate`)
  }

  public getForUser(userId: number, page: number, size: number): Observable<PaginatedResponse<CertificateResponse>> {
    return this.httpClient.get<PaginatedResponse<CertificateResponse>>(`${environment.baseUrl}/certificate/for-user/${userId}?page=${page}&size=${size}`);
  }
  
  public create(demandId: number): Observable<CertificateResponse> {
    return this.httpClient.post<CertificateResponse>(`${environment.baseUrl}/certificate/for-demand/${demandId}`, {});
  }

  public validate(serialNumber: string): Observable<string> {
    return this.httpClient.get<string>(`${environment.baseUrl}/certificate/${serialNumber}/validate`);
  }

  public validateFromUpload(uploadedCertificateRequest: UploadedCertificateRequest): Observable<string> {
    return this.httpClient.post<string>(`${environment.baseUrl}/certificate/validate-from-upload`, uploadedCertificateRequest);
  }

}
