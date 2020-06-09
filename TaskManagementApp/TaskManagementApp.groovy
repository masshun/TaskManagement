pipelineJob('pipelineJob'){
	definition{
		cps{
			script(readFileFromworkspace('TaskManagementApp.groovy'))
			sandbox()
		}
	}

}