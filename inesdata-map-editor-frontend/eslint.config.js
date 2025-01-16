// @ts-check

// Allows us to bring in the recommended core rules from eslint itself
const eslint = require("@eslint/js");

// Allows us to use the typed utility for our config, and to bring in the recommended rules for TypeScript projects from typescript-eslint
const tseslint = require("typescript-eslint");

// Allows us to bring in the recommended rules for Angular projects from angular-eslint
const angular = require("angular-eslint");

const simpleImportSort = require("eslint-plugin-simple-import-sort");

const unusedImports = require("eslint-plugin-unused-imports");

const eslintConfigPrettier = require("eslint-config-prettier");

// Export our config array, which is composed together thanks to the typed utility function from typescript-eslint
module.exports = tseslint.config(
	{
		ignores: ["projects/**/*"]
	},
	{
		// Everything in this config object targets our TypeScript files (Components, Directives, Pipes etc)
		files: ["**/*.ts"],
		extends: [
			// Apply the recommended core rules
			eslint.configs.recommended,
			// Apply the recommended TypeScript rules
			...tseslint.configs.recommended,
			// Optionally apply stylistic rules from typescript-eslint that improve code consistency
			...tseslint.configs.stylistic,
			// Apply the recommended Angular rules
			...angular.configs.tsRecommended
		],
		// Set the custom processor which will allow us to have our inline Component templates extracted
		// and treated as if they are HTML files (and therefore have the .html config below applied to them)
		processor: angular.processInlineTemplates,
		plugins: {
			"unused-imports": unusedImports,
			"simple-import-sort": simpleImportSort
		},
		rules: {
			"simple-import-sort/imports": "error",
			"simple-import-sort/exports": "error",
			"unused-imports/no-unused-imports": "error",
			"unused-imports/no-unused-vars": [
				"warn",
				{
					vars: "all",
					varsIgnorePattern: "^_",
					args: "after-used",
					argsIgnorePattern: "^_"
				}
			],
			"@angular-eslint/directive-selector": [
				"error",
				{
					type: "attribute",
					prefix: "app",
					style: "camelCase"
				}
			],
			"@angular-eslint/component-selector": [
				"error",
				{
					type: "element",
					prefix: "app",
					style: "kebab-case"
				}
			]
		}
	},
	{
		// Everything in this config object targets our HTML files (external templates,
		// and inline templates as long as we have the `processor` set on our TypeScript config above)
		files: ["**/*.html"],
		extends: [
			// Apply the recommended Angular template rules
			...angular.configs.templateRecommended,
			// Apply the Angular template rules which focus on accessibility of our apps
			...angular.configs.templateAccessibility
		],
		rules: {}
	},
	eslintConfigPrettier
);
