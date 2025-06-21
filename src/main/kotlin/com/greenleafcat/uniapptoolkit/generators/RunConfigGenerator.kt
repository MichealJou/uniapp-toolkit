package com.greenleafcat.uniapptoolkit.generators

class RunConfigGenerator(private val packageJson: Map<String, Any> ?) {

    companion object{
        private const val DEFAULT_PORT = 8080;
        private const val DEFAULT_TYPE = "h5";
    }
}
