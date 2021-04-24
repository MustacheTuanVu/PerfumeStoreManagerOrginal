import { IMember } from 'app/shared/model/member.model';

export interface IDayWorks {
  id?: number;
  workID?: string | null;
  userID?: string | null;
  dayWork?: number | null;
  member?: IMember | null;
}

export const defaultValue: Readonly<IDayWorks> = {};
