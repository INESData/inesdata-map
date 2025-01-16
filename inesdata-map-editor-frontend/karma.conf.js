// Karma configuration file, see link for more information
// https://karma-runner.github.io/1.0/config/configuration-file.html

module.exports = function (config) {
	config.set({
		basePath: "",
		frameworks: ["jasmine", "@angular-devkit/build-angular"],
		plugins: [require("karma-jasmine"), require("karma-chrome-launcher"), require("karma-jasmine-html-reporter"), require("karma-coverage"), require("@angular-devkit/build-angular/plugins/karma")],
		client: {
			// Leave Jasmine Spec Runner output visible in browser
			clearContext: false
		},
		coverageReporter: {
			// Output coverage file coverage/lcov.info
			dir: require("path").join(__dirname, "./coverage"),
			type: "lcov",
			subdir: "."
		},
		reporters: ["progress", "kjhtml", "coverage"],
		port: 9876,
		colors: true,
		logLevel: config.LOG_INFO,
		autoWatch: true,
		browsers: ["Chrome", "ChromeHeadless"],
		customLaunchers: {
			ChromeHeadless: {
				base: "Chrome",
				flags: ["--headless", "--disable-gpu", "--no-sandbox", "--remote-debugging-port=9222"]
			}
		},
		singleRun: false,
		restartOnFileChange: true
	});
};
