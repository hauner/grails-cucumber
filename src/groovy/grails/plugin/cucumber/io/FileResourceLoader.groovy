package grails.plugin.cucumber.io

import cucumber.io.ResourceLoader
import cucumber.io.Resource


class FileResourceLoader implements ResourceLoader {
    FileFilter filter

    FileResourceLoader (FileFilter filter) {
        this.filter = filter
    }

    @Override
    Iterable<Resource> resources (String path, String suffix) {
        File root = new File (path)
        return new FileResourceIterable (root, root, suffix, filter)
    }
}

