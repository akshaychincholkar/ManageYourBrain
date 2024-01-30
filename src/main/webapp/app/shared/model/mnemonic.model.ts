import dayjs from 'dayjs';
import { IMemory } from 'app/shared/model/memory.model';

export interface IMnemonic {
  id?: number;
  name?: string;
  creationDate?: dayjs.Dayjs | null;
  modifiedDate?: dayjs.Dayjs | null;
  memories?: IMemory[] | null;
}

export const defaultValue: Readonly<IMnemonic> = {};
