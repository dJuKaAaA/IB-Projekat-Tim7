import { UserRefResponse } from "./user-ref-response.model"

export interface CertificateResponse {
    id: number,
    serialNumber: string,
    type: string,
    issuer: UserRefResponse,
    issuedTo: UserRefResponse,
    startDate: string,
    endDate: string,
    publicKey: string,
    signature: string,
    isPulled: boolean
}