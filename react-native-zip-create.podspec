require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|
  s.name         = "react-native-zip-create"
  s.version      = package["version"]
  s.summary      = package["description"]
  s.description  = <<-DESC
                  react-native-zip-create
                   DESC
  s.homepage     = "https://github.com/jpsolero/react-native-zip-create"
  s.license      = "MIT"
  s.authors      = { "Javier Perez-Solero" => "jpsolero@mrmilu.com" }
  s.platform     = :ios, "8.0"
  s.source       = { :git => "https://github.com/jpsolero/react-native-zip-create.git", :tag => "#{s.version}" }

  s.source_files = "ios/**/*.{h,m,swift}"
  s.requires_arc = true

  s.dependency "React"
  s.dependency "Zip", "1.1"
	
  # s.dependency "..."
end

