import { DataBaseTypeEnum } from "../enums/database-type.enum";
import { DataFileTypeEnum } from "../enums/datafile-type.enum";
import { DataSourceTypeEnum } from "../enums/datasource-type.enum";

/**
 * Map database/file type to data source
 */
export function mapToDataSource(type: string): DataSourceTypeEnum {
	if (Object.values(DataBaseTypeEnum).includes(type as DataBaseTypeEnum)) {
		return DataSourceTypeEnum.DATABASE;
	} else if (Object.values(DataFileTypeEnum).includes(type as DataFileTypeEnum)) {
		return DataSourceTypeEnum.FILE;
	}
}
