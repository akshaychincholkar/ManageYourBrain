import dayjs from 'dayjs';
import { IAppUser } from 'app/shared/model/app-user.model';
import { ITag } from 'app/shared/model/tag.model';
import { IMnemonic } from 'app/shared/model/mnemonic.model';

export interface IMemory {
  id?: number;
  topic?: string;
  learnedDate?: dayjs.Dayjs;
  key?: string;
  comment?: string | null;
  creationDate?: dayjs.Dayjs | null;
  modifiedDate?: dayjs.Dayjs | null;
  appUser?: IAppUser | null;
  tag?: ITag | null;
  mnemonic?: IMnemonic | null;
}

export const defaultValue: Readonly<IMemory> = {};
