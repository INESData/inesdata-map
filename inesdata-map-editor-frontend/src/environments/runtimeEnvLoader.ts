import { Environment } from 'src/app/shared/models/environment.model';

export const runtimeEnvLoader = new Promise<Environment>((resolve) => {
	const xmlhttp = new XMLHttpRequest(),
		method = 'GET',
		url = './assets/config/runtime-environment.json';

	xmlhttp.open(method, url, true);

	xmlhttp.onload = function () {
		if (xmlhttp.status === 200) {
			resolve(JSON.parse(xmlhttp.responseText));
		} else {
			resolve(JSON.parse('{ "runtime": "" }'));
		}
	};

	xmlhttp.send();
});
