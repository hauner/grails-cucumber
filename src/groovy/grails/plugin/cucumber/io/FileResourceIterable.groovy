package grails.plugin.cucumber.io

import cucumber.io.Resource


class FileResourceIterable implements Iterable<Resource> {
    FileFilter filter
    String suffix
    File file
    File root

    FileResourceIterable (File root, File file, String suffix, FileFilter filter) {
        this.filter = filter
        this.suffix = suffix
        this.file = file
        this.root = root
    }

    Iterator<Resource> iterator () {
        new FileResourceIterator (root, file, suffix, filter)
    }
}

