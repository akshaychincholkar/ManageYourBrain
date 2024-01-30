import { IMemory } from 'app/shared/model/memory.model';

export interface IAppUser {
  id?: number;
  name?: string;
  email?: string;
  memories?: IMemory[] | null;
}

export const defaultValue: Readonly<IAppUser> = {};
