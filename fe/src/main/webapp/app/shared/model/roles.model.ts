import { IMember } from 'app/shared/model/member.model';

export interface IRoles {
  id?: number;
  roleID?: string | null;
  roleName?: string | null;
  members?: IMember[] | null;
}

export const defaultValue: Readonly<IRoles> = {};
