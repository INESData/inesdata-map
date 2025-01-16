import { Member } from './member.model';

/**
 * User model
 */
export interface User extends Member {
	name: string;
	surname: string;
	password?: string;
}
