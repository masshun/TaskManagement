TaskManagementApp('TaskManagementApp'){
	definition{
		cps{
			script(readFileFromworkspace('TaskManagementApp.groovy'))
			sandbox()
		}
	}

}