import { UserRefResponse } from "./user-ref-response.model";

export interface CertificateDemandResponse {
    id: number,
    type: string,
    requestedSigningCertificateId: number,
    requestedIssuer: UserRefResponse,
    requester: UserRefResponse,
    reason: string,
    status: string
}